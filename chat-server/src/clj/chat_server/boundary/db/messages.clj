(ns chat-server.boundary.db.messages
  (:require [chat-server.boundary.db.channels :as channels]
            [chat-server.boundary.db.core :as db]
            [chat-server.boundary.db.users :as users]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql])
  (:import (java.time ZonedDateTime)))

(s/def ::id integer?)
(s/def ::body string?)
(s/def ::user-id ::users/id)
(s/def ::channel-id ::channels/id)
(s/def ::date #(instance? ZonedDateTime %))
(s/def ::user-uid ::users/uid)
(s/def ::user-name ::users/name)
(s/def ::user-avatar ::users/avatar)
(s/def ::channel-name ::channels/name)
(s/def ::message (s/keys :req-un [::body ::user-id ::channel-id]
                         :opt-un [::id ::date ::user-uid ::user-name ::user-avatar ::channel-name]))

(s/fdef create-message
  :args (s/cat :db ::db/db
               :message ::message)
  :ret ::id)

(s/fdef create-messages
  :args (s/cat :db ::db/db
               :messages (s/coll-of ::message))
  :ret (s/coll-of ::id))

(s/fdef find-messages-by-channel
  :args (s/cat :db ::db/db
               :channel-id ::channel-id)
  :ret (s/coll-of ::message))

(defprotocol Messages
  (create-message [db message])
  (create-messages [db messages])
  (find-messages-by-channel [db channel-id]))

(extend-protocol Messages
  duct.database.sql.Boundary
  (create-message [db message]
    (db/insert! db :messages message))
  (create-messages [db messages]
    (db/insert-multi! db :messages messages))
  (find-messages-by-channel [db channel-id]
    (db/select db (sql/build
                   :select [:m.*
                            [:u.uid :user-uid]
                            [:u.name :user-name]
                            [:u.avatar :user-avatar]
                            [:c.name :channel-name]]
                   :from [[:messages :m]]
                   :join [[:users :u] [:= :m.user-id :u.id]
                          [:channels :c] [:= :m.channel-id :c.id]]
                   :where [:= :m.channel-id channel-id]
                   :order-by [[:m.date :desc] [:m.id :desc]]
                   :limit 20))))
