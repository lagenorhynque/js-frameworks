(ns chat-server.handler.channels
  (:require [chat-server.boundary.db.channels :as db.channels]
            [chat-server.boundary.db.messages :as db.messages]
            [ring.util.http-response :as response]
            [struct.core :as st]))

(def validations
  {::list-channel-messages [[:channel-id st/required st/number-str]]
   ::create-channel [[:name st/required st/string]]
   ::create-message [[:channel-id st/required st/number-str]
                     [:user-id st/required st/number-str]
                     [:body st/required st/string]]})

(defn list-channels [{:keys [db]}]
  (response/ok {:data (db.channels/find-channels db)}))

(defn list-channel-messages [{:keys [db tx-data]}]
  (response/ok {:data (db.messages/find-messages-by-channel db (:channel-id tx-data))}))

(defn create-channel [{:keys [db tx-data]}]
  (let [channel-name (:name tx-data)
        channel-id (db.channels/create-channel db {:name channel-name})
        messages [{:body (str "Welcome to " channel-name " channel!")
                   :user-id 1
                   :channel-id channel-id}
                  {:body "はじめてのメッセージを投稿してみましょう。"
                   :user-id 1
                   :channel-id channel-id}]]
    (db.messages/create-messages db messages)
    (response/ok {:data {:id channel-id}})))

(defn create-message [{:keys [db tx-data]}]
  (let [message-id (db.messages/create-message db tx-data)]
    (response/ok {:data {:id message-id}})))
