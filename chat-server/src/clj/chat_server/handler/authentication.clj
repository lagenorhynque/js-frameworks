(ns chat-server.handler.authentication
  (:require [chat-server.boundary.db.users :as db.users]
            [ring.util.http-response :as response]
            [struct.core :as st]))

(def validations
  {::login [[:uid st/required st/string]]})

(defn fetch-user [{:keys [session]}]
  (response/ok {:data (:user session)}))

(defn login [{:keys [db tx-data]}]
  (if-let [user (db.users/find-user-by-uid db (:uid tx-data))]
    (assoc (response/ok {:data user})
           :session {:user user})
    (response/not-found {:errors {:uid "doesn't exist"}})))

(defn logout [_]
  (assoc (response/no-content)
         :session nil))
