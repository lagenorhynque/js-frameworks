(ns chat-server.test-helper.db-data)

(def channels
  [{:id 1
    :name "general"}
   {:id 2
    :name "random"}])

(def messages
  [{:id 1
    :body "Welcome to general channel!"
    :user-id 1
    :channel-id 1
    :date #time/inst "2018-08-24T12:00:00+09:00"}
   {:id 2
    :body "はじめてのメッセージを投稿してみましょう。"
    :user-id 1
    :channel-id 1
    :date #time/inst "2018-08-24T12:00:00+09:00"}
   {:id 3
    :body "Welcome to random channel!"
    :user-id 1
    :channel-id 1
    :date #time/inst "2018-08-24T12:00:00+09:00"}
   {:id 4
    :body "はじめてのメッセージを投稿してみましょう。"
    :user-id 1
    :channel-id 1
    :date #time/inst "2018-08-24T12:00:00+09:00"}])

(def users
  [{:id 1
    :uid "anon"
    :name "Anonymous"
    :avatar ""}
   {:id 2
    :uid "lagenorhynque"
    :name "lagénorhynque"
    :avatar ""}])
