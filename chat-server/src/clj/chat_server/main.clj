(ns chat-server.main
  (:gen-class)
  (:require [chat-server.boundary.db.core]
            [clojure.java.io :as io]
            [duct.core :as duct]
            [integrant.core :as ig]
            [ragtime.jdbc]
            [ragtime.repl]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys (or (duct/parse-keys args) [:duct/daemon])]
    (-> (duct/read-config (io/resource "chat_server/config.edn"))
        (duct/prep keys)
        (ig/prep keys)
        (duct/exec keys))))

(defn- load-migration-config [url]
  {:datastore (ragtime.jdbc/sql-database url)
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn db-migrate [database-url]
  (ragtime.repl/migrate (load-migration-config database-url)))

(defn db-rollback [database-url]
  (ragtime.repl/rollback (load-migration-config database-url)))
