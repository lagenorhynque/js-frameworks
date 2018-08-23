(ns chat-server.handler.authentication-test
  (:require [chat-server.handler.authentication :as sut]
            [chat-server.test-helper.core :as helper :refer [with-db-data with-session with-system]]
            [chat-server.test-helper.db-data :as db-data]
            [clojure.test :as t]))

(t/use-fixtures
  :once
  helper/instrument-specs)

(t/deftest test-login-logout
  (with-system [sys (helper/test-system)]
    (with-db-data [sys {:users db-data/users}]
      (t/testing "validation error"
        (let [{:keys [status body]}
              (helper/http-post sys "/api/authentication/login"
                                (helper/->json {}))]
          (t/is (= 400 status))
          (t/is (= [:uid] (-> body helper/<-json :errors keys)))))
      (t/testing "specified user doesn't exist"
        (let [{:keys [status body]}
              (helper/http-post sys "/api/authentication/login"
                                (helper/->json {:uid "yosoro"}))]
          (t/is (= 404 status))
          (t/is (= {:errors {:uid "doesn't exist"}} (helper/<-json body)))))
      (with-session
        (t/testing "login successfully"
          (let [{:keys [status body]}
                (helper/http-post sys "/api/authentication/login"
                                  (helper/->json {:uid "lagenorhynque"}))]
            (t/is (= 200 status))
            (t/is (= {:data {:id 2
                             :uid "lagenorhynque"
                             :name "lagénorhynque"
                             :avatar ""}} (helper/<-json body)))))
        (t/testing "user info is stored in session after login"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/authentication")]
            (t/is (= 200 status))
            (t/is (= {:data {:id 2
                             :uid "lagenorhynque"
                             :name "lagénorhynque"
                             :avatar ""}} (helper/<-json body)))))
        (t/testing "logout successfully"
          (let [{:keys [status body]}
                (helper/http-delete sys "/api/authentication/logout")]
            (t/is (= 204 status))))
        (t/testing "user info is deleted from session after logout"
          (let [{:keys [status body]}
                (helper/http-get sys "/api/authentication")]
            (t/is (= 401 status))))))))
