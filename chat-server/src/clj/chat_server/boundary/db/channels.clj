(ns chat-server.boundary.db.channels
  (:require [chat-server.boundary.db.core :as db]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql]))

(s/def ::id integer?)
(s/def ::name string?)
(s/def ::channel (s/keys :req-un [::name]
                         :opt-un [::id]))

(s/fdef create-channel
  :args (s/cat :db ::db/db
               :channel ::channel)
  :ret ::id)

(s/fdef find-channels
  :args (s/cat :db ::db/db)
  :ret (s/coll-of ::channel))

(s/fdef find-channel-by-id
  :args (s/cat :db ::db/db
               :channel-id ::id)
  :ret (s/nilable ::channel))

(defprotocol Channels
  (create-channel [db channel])
  (find-channels [db])
  (find-channel-by-id [db channel-id]))

(extend-protocol Channels
  duct.database.sql.Boundary
  (create-channel [db channel]
    (db/insert! db :channels channel))
  (find-channels [db]
    (db/select db (sql/build
                   :select :*
                   :from :channels
                   :order-by [[:name :asc] [:id :asc]])))
  (find-channel-by-id [db channel-id]
    (db/select-one db (sql/build
                       :select :*
                       :from :channels
                       :where [:= :id channel-id]))))
