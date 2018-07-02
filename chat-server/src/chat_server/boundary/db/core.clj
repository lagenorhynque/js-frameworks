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
  (jdbc/query db (sql/format sql-map)
              {:identifiers util/->kebab-case
               :entities (jdbc/quoted \`)}))

(defn select-one [db sql-map]
  (first (select db sql-map)))

(defn insert-multi! [{db :spec} table row-maps]
  (->> (jdbc/insert-multi! db table row-maps
                           {:identifiers util/->kebab-case
                            :entities (comp (jdbc/quoted \`)
                                            util/->snake_case)})
       (map :generated_key)))

(defn insert! [db table row-map]
  (first (insert-multi! db table [row-map])))
