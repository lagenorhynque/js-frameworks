(ns chat-server.boundary.db.users
  (:require [chat-server.boundary.db.core :as db]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql]))

(s/def ::id integer?)
(s/def ::uid string?)
(s/def ::name string?)
(s/def ::avatar string?)
(s/def ::user (s/keys :req-un [::uid ::name]
                      :opt-un [::id ::avatar]))

(s/fdef create-user
  :args (s/cat :db ::db/db
               :user ::user)
  :ret ::id)

(defprotocol Users
  (create-user [db user])
  (find-user-by-id [db id]))

(extend-protocol Users
  duct.database.sql.Boundary
  (create-user [db user]
    (db/insert! db :users user))
  (find-user-by-id [db user-id]
    (db/select-one db (sql/build
                       :select :*
                       :from :users
                       :where [:= :id user-id]))))
