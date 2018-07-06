(ns chat-server.server
  (:require [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defmethod ig/init-key ::service
  [_ service]
  (assoc service
         ;; all origins are allowed in dev mode
         ::http/allowed-origins {:creds true :allowed-origins (constantly true)}))

(defmethod ig/init-key ::server
  [_ {:keys [service dev?]}]
  (println (str "\nCreating your " (when dev? "[DEV] ") "server..."))
  (cond-> service
    ;; Wire up interceptor chains
    true http/default-interceptors
    dev? http/dev-interceptors
    true http/create-server
    true http/start))

(defmethod ig/halt-key! ::server
  [_ server]
  (http/stop server))
