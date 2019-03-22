(ns chat-server.test-helper.core
  (:require [chat-server.boundary.db.core]
            [chat-server.test-helper.db :refer [insert-db-data! truncate-all-tables!]]
            [chat-server.util.core :as util]
            [cheshire.core :as cheshire]
            [clj-http.client :as client]
            [clj-http.cookies]
            [clj-http.core]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [duct.core :as duct]
            [integrant.core :as ig]
            [orchestra.spec.test :as stest]))

(duct/load-hierarchy)

;;; fixtures

(defn instrument-specs [f]
  (stest/instrument)
  (f))

;;; macros for testing context

(defn test-system []
  (-> (io/resource "chat_server/config.edn")
      duct/read-config
      (duct/prep-config [:duct.profile/dev :duct.profile/test])))

(s/fdef with-system
  :args (s/cat :binding (s/coll-of any?
                                   :kind vector
                                   :count 2)
               :body (s/* any?)))

(defmacro with-system [[bound-var binding-expr] & body]
  `(let [~bound-var (ig/init ~binding-expr)]
     (try
       ~@body
       (finally (ig/halt! ~bound-var)))))

(s/fdef with-db-data
  :args (s/cat :binding (s/coll-of any?
                                   :kind vector
                                   :count 2)
               :body (s/* any?)))

(defmacro with-db-data [[system db-data-map] & body]
  `(let [db# (:duct.database.sql/hikaricp ~system)]
     (try
       (insert-db-data! db# ~db-data-map)
       ~@body
       (finally (truncate-all-tables! db#)))))

(s/fdef with-session
  :args (s/cat :body (s/* any?)))

(defmacro with-session [& body]
  `(binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
     ~@body))

(def test-login-user {:uid "lagenorhynque"})

(declare http-post http-delete ->json)

(s/fdef with-logged-in-session
  :args (s/cat :binding (s/coll-of any?
                                   :kind vector
                                   :count 2)
               :body (s/* any?)))

(defmacro with-logged-in-session [[system login-user] & body]
  `(with-session
     (try
       (assert (= 200 (:status (http-post ~system "/api/authentication"
                                          (->json ~login-user)))))
       ~@body
       (finally (assert (= 204 (:status (http-delete ~system "/api/authentication"))))))))

;;; HTTP client

(def ^:private url-prefix "http://localhost:")

(defn- server-port [system]
  (get-in system [:duct.server/pedestal :io.pedestal.http/port]))

(defn http-get [system url & {:as options}]
  (client/get (str url-prefix (server-port system) url)
              (merge {:accept :json
                      :throw-exceptions? false} options)))

(defn http-post [system url body & {:as options}]
  (client/post (str url-prefix (server-port system) url)
               (merge {:body body
                       :content-type :json
                       :accept :json
                       :throw-exceptions? false} options)))

(defn http-put [system url body & {:as options}]
  (client/put (str url-prefix (server-port system) url)
              (merge {:body body
                      :content-type :json
                      :accept :json
                      :throw-exceptions? false} options)))

(defn http-delete [system url & {:as options}]
  (client/delete (str url-prefix (server-port system) url)
                 (merge {:accept :json
                         :throw-exceptions? false} options)))

;;; JSON conversion

(defn ->json [obj]
  (-> obj
      util/transform-keys-to-snake
      cheshire/generate-string))

(defn <-json [str]
  (-> str
      (cheshire/parse-string true)
      util/transform-keys-to-kebab))

;;; misc.

(defn json-errors-keyset
  "JSON文字列をオブジェクトに変換したときの `:errors` のキーのセットを返す。"
  [json]
  (-> json <-json :errors keys set))
