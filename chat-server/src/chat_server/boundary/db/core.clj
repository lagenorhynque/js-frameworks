(ns chat-server.boundary.db.core
  (:require [chat-server.util :as util]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql]
            [integrant.core :as ig]
            [ragtime.jdbc :refer [load-resources]]))

(defmethod ig/prep-key :duct.migrator/ragtime [_ config]
  (assoc config :migrations (load-resources "migrations")))

(s/def ::db #(instance? duct.database.sql.Boundary %))

(defn select [{db :spec} sql-map]
  (->> sql-map
       sql/format
       (jdbc/query db)
       util/transform-keys-to-kebab))

(defn select-one [{db :spec} sql-map]
  (->> sql-map
       sql/format
       (jdbc/query db)
       util/transform-keys-to-kebab
       first))

(defn insert! [{db :spec} table row-map]
  (->> row-map
       util/transform-keys-to-snake
       (jdbc/insert! db table)
       first
       :generated_key))

(defn insert-multi! [{db :spec} table row-maps]
  (->> row-maps
       util/transform-keys-to-snake
       (jdbc/insert-multi! db table)
       (map :generated_key)))
