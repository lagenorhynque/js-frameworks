(ns chat-server.boundary.db.core
  (:require [chat-server.util.core :as util]
            [chat-server.util.instant]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql]
            [integrant.core :as ig]
            [ragtime.jdbc :refer [load-resources]])
  (:import (java.time ZonedDateTime ZoneId)))

;;; JDBC date time conversion

(extend-protocol jdbc/IResultSetReadColumn
  java.sql.Timestamp
  (result-set-read-column [v _ _]
    (ZonedDateTime/ofInstant (.toInstant v) (ZoneId/systemDefault))))

(extend-protocol jdbc/ISQLValue
  ZonedDateTime
  (sql-value [v]
    (java.sql.Timestamp/from (.toInstant v))))

;;; DB migration

(defmethod ig/prep-key :duct.migrator/ragtime [_ config]
  (assoc config :migrations (load-resources "migrations")))

;;; DB access utilities

(s/def ::db any?)
(s/def ::sql-map (s/map-of keyword? any?))
(s/def ::table keyword?)
(s/def ::row-map (s/map-of keyword? any?))
(s/def ::row-count nat-int?)
(s/def ::row-id pos-int?)

(s/fdef with-transaction
  :args (s/cat :binding (s/tuple any?)
               :body (s/* any?)))

(defmacro with-transaction [[db] & body]
  (if (simple-symbol? db)
    `(jdbc/with-db-transaction [~db (:spec ~db)]
       (let [~db (duct.database.sql/->Boundary ~db)]
         ~@body))
    `(jdbc/with-db-transaction [~'db (:spec ~db)]
       (let [~'db (duct.database.sql/->Boundary ~'db)]
         ~@body))))

(s/fdef select
  :args (s/cat :db ::db
               :sql-map ::sql-map)
  :ret (s/coll-of ::row-map))

(defn select [{:keys [spec]} sql-map]
  (jdbc/query spec (sql/format sql-map :quoting :mysql)
              {:identifiers util/->kebab-case}))

(s/fdef select-one
  :args (s/cat :db ::db
               :sql-map ::sql-map)
  :ret (s/nilable ::row-map))

(defn select-one [db sql-map]
  (first (select db sql-map)))

(s/fdef insert!
  :args (s/cat :db ::db
               :table ::table
               :row-map ::row-map)
  :ret ::row-id)

(defn insert! [{:keys [spec]} table row-map]
  (-> (jdbc/insert! spec table row-map {:entities (comp (jdbc/quoted \`)
                                                        util/->snake_case)})
      first
      :generated_key))

(s/fdef insert-multi!
  :args (s/cat :db ::db
               :table ::table
               :row-maps (s/coll-of ::row-map :min-count 1))
  :ret ::row-count)

(defn insert-multi! [{:keys [spec]} table row-maps]
  (first (jdbc/execute! spec (sql/format (sql/build :insert-into table
                                                    :values row-maps)
                                         :quoting :mysql))))
