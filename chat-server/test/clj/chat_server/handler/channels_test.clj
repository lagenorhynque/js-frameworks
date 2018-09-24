(ns chat-server.handler.channels-test
  (:require [chat-server.handler.channels :as sut]
            [chat-server.test-helper.core :as helper :refer [with-db-data with-logged-in-session with-system]]
            [chat-server.test-helper.db-data :as db-data]
            [clojure.test :as t]))

(t/use-fixtures
  :once
  helper/instrument-specs)

(t/deftest test-list-channels
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:channels db-data/channels
                        :messages db-data/messages
                        :users db-data/users}]
      (t/testing "ログインしていないとエラー"
        (let [{:keys [status]}
              (helper/http-get sys "/api/channels")]
          (t/is (= 401 status))))
      (with-logged-in-session [sys helper/test-login-user]
        (t/testing "チャンネルの一覧が取得できる"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/channels")]
            (t/is (= 200 status))
            (t/is (= {:data [{:id 1
                              :name "general"}
                             {:id 2
                              :name "random"}]}
                     (helper/<-json body)))))))))

(t/deftest test-create-channel
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:channels db-data/channels
                        :messages db-data/messages
                        :users db-data/users}]
      (let [channel (helper/->json {:name "times_twitter"})]
        (t/testing "ログインしていないとエラー"
          (let [{:keys [status]}
                (helper/http-post sys "/api/channels" channel)]
            (t/is (= 401 status))))
        (with-logged-in-session [sys helper/test-login-user]
          (t/testing "バリデーションエラー"
            (let [{:keys [status body]}
                  (helper/http-post sys "/api/channels" (helper/->json {}))]
              (t/is (= 400 status))
              (t/is (= #{:name} (-> body helper/<-json :errors keys set)))))
          (t/testing "チャンネルが新規作成できる"
            (let [{:keys [status body]}
                  (helper/http-post sys "/api/channels" channel)]
              (t/is (= 200 status))
              (t/is (= {:data {:id 3}} (helper/<-json body)))
              (t/testing "作成されたチャンネルの情報が取得できる"
                (let [{:keys [body]}
                      (helper/http-get sys "/api/channels/3")]
                  (t/is (= {:id 3
                            :name "times_twitter"}
                           (-> body helper/<-json :data)))))
              (t/testing "作成されたチャンネルに紐付く初期メッセージが取得できる"
                (let [{:keys [body]}
                      (helper/http-get sys "/api/channels/3/messages")]
                  (t/is (= [{:id 7
                             :body "はじめてのメッセージを投稿してみましょう。"
                             :user-id 2
                             :channel-id 3
                             :user-uid "lagenorhynque"
                             :user-name "lagénorhynque"
                             :user-avatar ""
                             :channel-name "times_twitter"}
                            {:id 6
                             :body "Welcome to times_twitter channel!"
                             :user-id 2
                             :channel-id 3
                             :user-uid "lagenorhynque"
                             :user-name "lagénorhynque"
                             :user-avatar ""
                             :channel-name "times_twitter"}]
                           (map #(select-keys % [:id :body :user-id :channel-id :user-uid :user-name :user-avatar :channel-name])
                                (-> body helper/<-json :data)))))))))))))

(t/deftest test-fetch-channel
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:channels db-data/channels
                        :messages db-data/messages
                        :users db-data/users}]
      (t/testing "ログインしていないとエラー"
        (let [{:keys [status]}
              (helper/http-get sys "/api/channels/2")]
          (t/is (= 401 status))))
      (with-logged-in-session [sys helper/test-login-user]
        (t/testing "バリデーションエラー"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/channels/foo")]
            (t/is (= 400 status))
            (t/is (= #{:channel-id} (-> body helper/<-json :errors keys set)))))
        (t/testing "チャンネルが存在しないとエラー"
          (let [{:keys [status]}
                (helper/http-get sys "/api/channels/100")]
            (t/is (= 404 status))))
        (t/testing "チャンネルの情報が取得できる"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/channels/2")]
            (t/is (= 200 status))
            (t/is (= {:data {:id 2
                             :name "random"}}
                     (helper/<-json body)))))))))

(t/deftest test-list-channel-messages
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:channels db-data/channels
                        :messages db-data/messages
                        :users db-data/users}]
      (t/testing "ログインしていないとエラー"
        (let [{:keys [status]}
              (helper/http-get sys "/api/channels/2/messages")]
          (t/is (= 401 status))))
      (with-logged-in-session [sys helper/test-login-user]
        (t/testing "バリデーションエラー"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/channels/foo/messages")]
            (t/is (= 400 status))
            (t/is (= #{:channel-id} (-> body helper/<-json :errors keys set)))))
        (t/testing "チャンネルが存在しないとエラー"
          (let [{:keys [status]}
                (helper/http-get sys "/api/channels/100/messages")]
            (t/is (= 404 status))))
        (t/testing "チャンネルに紐付くメッセージの一覧が取得できる"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/channels/2/messages")]
            (t/is (= 200 status))
            (t/is (= [{:id 5
                       :body "おはヨーソロー(*> ᴗ •*)ゞ"
                       :user-id 2
                       :channel-id 2
                       :user-uid "lagenorhynque"
                       :user-name "lagénorhynque"
                       :user-avatar ""
                       :channel-name "random"}
                      {:id 4
                       :body "はじめてのメッセージを投稿してみましょう。"
                       :user-id 1
                       :channel-id 2
                       :user-uid "anon"
                       :user-name "Anonymous"
                       :user-avatar ""
                       :channel-name "random"}
                      {:id 3
                       :body "Welcome to random channel!"
                       :user-id 1
                       :channel-id 2
                       :user-uid "anon"
                       :user-name "Anonymous"
                       :user-avatar ""
                       :channel-name "random"}]
                     (map #(select-keys % [:id :body :user-id :channel-id :user-uid :user-name :user-avatar :channel-name])
                          (-> body helper/<-json :data))))))))))

(t/deftest test-create-message
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:channels db-data/channels
                        :messages db-data/messages
                        :users db-data/users}]
      (let [message (helper/->json {:body "テンション上がるにゃー＞ω＜/"})]
        (t/testing "ログインしていないとエラー"
          (let [{:keys [status]}
                (helper/http-post sys "/api/channels/1/messages" message)]
            (t/is (= 401 status))))
        (with-logged-in-session [sys helper/test-login-user]
          (t/testing "バリデーションエラー"
            (let [{:keys [status body]}
                  (helper/http-post sys "/api/channels/foo/messages" (helper/->json {}))]
              (t/is (= 400 status))
              (t/is (= #{:channel-id :body} (-> body helper/<-json :errors keys set)))))
          (t/testing "チャンネルが存在しないとエラー"
            (let [{:keys [status]}
                  (helper/http-post sys "/api/channels/100/messages" message)]
              (t/is (= 404 status))))
          (t/testing "メッセージが新規作成できる"
            (let [{:keys [status body]}
                  (helper/http-post sys "/api/channels/1/messages"
                                    message)]
              (t/is (= 200 status))
              (t/is (= {:data {:id 6}} (helper/<-json body)))
              (t/testing "作成されたメッセージを含むメッセージの一覧が取得できる"
                (let [{:keys [body]}
                      (helper/http-get sys "/api/channels/1/messages")]
                  (t/is (= [{:id 6
                             :body "テンション上がるにゃー＞ω＜/"
                             :user-id 2
                             :channel-id 1
                             :user-uid "lagenorhynque"
                             :user-name "lagénorhynque"
                             :user-avatar ""
                             :channel-name "general"}
                            {:id 2
                             :body "はじめてのメッセージを投稿してみましょう。"
                             :user-id 1
                             :channel-id 1
                             :user-uid "anon"
                             :user-name "Anonymous"
                             :user-avatar ""
                             :channel-name "general"}
                            {:id 1
                             :body "Welcome to general channel!"
                             :user-id 1
                             :channel-id 1
                             :user-uid "anon"
                             :user-name "Anonymous"
                             :user-avatar ""
                             :channel-name "general"}]
                           (map #(select-keys % [:id :body :user-id :channel-id :user-uid :user-name :user-avatar :channel-name])
                                (-> body helper/<-json :data)))))))))))))
