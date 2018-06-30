(ns chat-server.service
  (:require [chat-server.routes :as routes]
            [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defmethod ig/init-key :app/service
  [_ service]
  (assoc service
         ::http/routes routes/routes
         ;; Routes can be a function that resolve routes,
         ;;  we can use this to set the routes to be reloadable
         ::http/routes #(route/expand-routes (deref #'routes/routes))
         ;; all origins are allowed in dev mode
         ::http/allowed-origins {:creds true :allowed-origins (constantly true)}))

(defmethod ig/init-key :app/server
  [_ {:keys [service dev?]}]
  (println (str "\nCreating your " (when dev? "[DEV] ") "server..."))
  (cond-> service
    ;; Wire up interceptor chains
    true http/default-interceptors
    dev? http/dev-interceptors
    true http/create-server
    true http/start))

(defmethod ig/halt-key! :app/server
  [_ server]
  (http/stop server))
