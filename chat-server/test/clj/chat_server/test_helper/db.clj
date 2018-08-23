(ns chat-server.test-helper.db
  (:require [chat-server.boundary.db.core :as db]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as s]
            [honeysql.core :as sql]))

(s/def ::name string?)
(s/def ::table (s/keys :req-un [::name]))

(s/fdef select-tables
  :args (s/cat :db ::db/db)
  :ret (s/coll-of ::table))

(defn select-tables [db]
  (db/select db (sql/build
                 :select [[(sql/call :concat :table-schema "." :table-name) :name]]
                 :from :information-schema.tables
                 :where [:and
                         [:= :table-type "BASE TABLE"]
                         [:not= :table-name "ragtime_migrations"]])))

(s/fdef truncate-table!
  :args (s/cat :db ::db/db
               :table ::table)
  :ret any?)

(defn truncate-table! [{:keys [spec]} table]
  (jdbc/execute! spec [(str "truncate table " (:name table))]))

(s/fdef set-foreign-key-checks!
  :args (s/cat :db ::db/db
               :enabled? boolean?)
  :ret any?)

(defn set-foreign-key-checks! [{:keys [spec]} enabled?]
  (jdbc/execute! spec [(str "set @@session.foreign_key_checks = "
                            (if enabled? 1 0))]))

(s/fdef truncate-all-tables!
  :args (s/cat :db ::db/db)
  :ret any?)

(defn truncate-all-tables! [db]
  (set-foreign-key-checks! db false)
  (doseq [table (select-tables db)]
    (truncate-table! db table))
  (set-foreign-key-checks! db true))
