(ns chat-server.handler.channels
  (:require [chat-server.boundary.db.channels :as db.channels]
            [chat-server.boundary.db.core :refer [with-transaction]]
            [chat-server.boundary.db.messages :as db.messages]
            [chat-server.boundary.db.users :as db.users]
            [ring.util.http-response :as response]
            [struct.core :as st]))

(def validations
  {::list-channel-messages [[:channel-id st/required st/number-str]]
   ::create-channel [[:name st/required st/string]]
   ::fetch-channel [[:channel-id st/required st/number-str]]
   ::create-message [[:channel-id st/required st/number-str]
                     [:body st/required st/string]]})

(defn list-channels [{:keys [db]}]
  (response/ok {:data (db.channels/find-channels db)}))

(defn create-channel [{:keys [db session tx-data]}]
  (with-transaction [db]
    (let [user-id (get-in session [:user :id])
          channel-name (:name tx-data)
          channel-id (db.channels/create-channel db {:name channel-name})
          messages [{:body (str "Welcome to " channel-name " channel!")
                     :user-id user-id
                     :channel-id channel-id}
                    {:body "はじめてのメッセージを投稿してみましょう。"
                     :user-id user-id
                     :channel-id channel-id}]]
      (db.messages/create-messages db messages)
      (response/ok {:data {:id channel-id}}))))

(defn fetch-channel [{:keys [db tx-data]}]
  (if-let [channel (db.channels/find-channel-by-id db (:channel-id tx-data))]
    (response/ok {:data channel})
    (response/not-found {:errors {:channel-id "doesn't exist"}})))

(defn list-channel-messages [{:keys [db tx-data]}]
  (if (db.channels/find-channel-by-id db (:channel-id tx-data))
    (response/ok {:data (db.messages/find-messages-by-channel db (:channel-id tx-data))})
    (response/not-found {:errors {:channel-id "doesn't exist"}})))

(defn create-message [{:keys [db session tx-data]}]
  (with-transaction [db]
    (if (db.channels/find-channel-by-id db (:channel-id tx-data))
      (let [user-id (get-in session [:user :id])
            message-id (db.messages/create-message db (assoc tx-data
                                                             :user-id user-id))]
        (response/ok {:data {:id message-id}}))
      (response/not-found {:errors {:channel-id "doesn't exist"}}))))
