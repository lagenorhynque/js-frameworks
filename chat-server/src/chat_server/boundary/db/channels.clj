(ns chat-server.boundary.db.channels
  (:require [chat-server.boundary.db.core :as db]
            [clojure.spec.alpha :as s]
            [duct.database.sql]
            [honeysql.core :as sql]))

(s/def ::id pos-int?)
(s/def ::name string?)
(s/def ::channel (s/keys :req-un [::name]
                         :opt-un [::id]))

(s/fdef create-channel
  :args (s/cat :db ::db/db
               :channel ::channel)
  :ret ::id)

(defprotocol Channels
  (create-channel [db channel]))

(extend-protocol Channels
  duct.database.sql.Boundary
  (create-channel [db channel]
    (db/insert! db :channels channel)))
