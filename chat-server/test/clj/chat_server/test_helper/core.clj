(ns chat-server.test-helper.core
  (:require [chat-server.boundary.db.core]
            [chat-server.test-helper.db :refer [truncate-all-tables!]]
            [chat-server.util.core :as util]
            [cheshire.core :as cheshire]
            [clj-http.client :as client]
            [clj-http.cookies]
            [clj-http.core]
            [clojure.java.io :as io]
            [clojure.spec.test.alpha :as stest]
            [duct.core :as duct]
            [integrant.core :as ig]))

(duct/load-hierarchy)

;;; fixtures

(defn instrument-specs [f]
  (stest/instrument)
  (f))

;;; macros for testing context

(defn test-system []
  (-> (duct/read-config (io/resource "test.edn"))
      duct/prep
      ig/prep))

(defmacro with-system [[bound-var binding-expr] & body]
  `(let [~bound-var (ig/init ~binding-expr)]
     (try
       ~@body
       (finally
         (ig/halt! ~bound-var)))))

(defmacro with-db-data [[system db-data-map] & body]
  `(let [db# (:duct.database.sql/hikaricp ~system)]
     (try
       (doseq [[table# records#] ~db-data-map]
         (chat-server.boundary.db.core/insert-multi! db# table# records#))
       ~@body
       (finally (truncate-all-tables! db#)))))

(defmacro with-session [& body]
  `(binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
     ~@body))

;;; HTTP client

(def ^:private url-prefix "http://localhost:")

(defn- server-port [system]
  (get-in system [:chat-server.server/service :io.pedestal.http/port]))

(defn http-get [system url & options]
  (client/get (str url-prefix (server-port system) url)
              (merge {:accept :json
                      :throw-exceptions? false} options)))

(defn http-post [system url body & options]
  (client/post (str url-prefix (server-port system) url)
               (merge {:body body
                       :content-type :json
                       :accept :json
                       :throw-exceptions? false} options)))

(defn http-put [system url body & options]
  (client/put (str url-prefix (server-port system) url)
              (merge {:body body
                      :content-type :json
                      :accept :json
                      :throw-exceptions? false} options)))

(defn http-delete [system url & options]
  (client/delete (str url-prefix (server-port system) url)
                 (merge {:accept :json
                         :throw-exceptions? false} options)))

;;; JSON conversion

(defn ->json [obj]
  (-> obj
      cheshire/generate-string
      util/transform-keys-to-snake))

(defn <-json [str]
  (-> str
      (cheshire/parse-string true)
      util/transform-keys-to-kebab))
