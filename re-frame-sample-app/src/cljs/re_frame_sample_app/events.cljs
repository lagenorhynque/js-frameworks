(ns re-frame-sample-app.events
  (:require [ajax.core :as ajax]
            [re-frame-sample-app.db :as db]
            [re-frame-sample-app.fx :as fx]
            [re-frame.core :as re-frame]))

(def base-url "http://localhost:8080/api")

(re-frame/reg-event-fx
  ::initialize-db
  (fn [{:keys [db]} _]
    {:db db/default-db
     :dispatch [::fetch-channels]}))

(re-frame/reg-event-fx
  ::set-current-route
  (fn [{:keys [db]} [_ route]]
    {:db (assoc db :route route)
     ::fx/init-view route}))

(re-frame/reg-event-fx
  ::navigate
  (fn [_ [_ handler route-params]]
    {::fx/navigate {:handler handler
                    :route-params route-params}}))

(re-frame/reg-event-fx
  ::fetch-channels
  (fn [_ _]
    {:http-xhrio {:method :get
                  :uri (str base-url "/channels")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::set-channels]}}))

(re-frame/reg-event-db
  ::set-channels
  (fn [db [_ response]]
    (assoc db :channels (:data response))))

(re-frame/reg-event-fx
  ::fetch-channel-detail
  (fn [_ [_ channel-id]]
    {:http-xhrio {:method :get
                  :uri (str base-url "/channels/" channel-id)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::set-channel-detail]}}))

(re-frame/reg-event-db
  ::set-channel-detail
  (fn [db [_ response]]
    (assoc-in db [:channel :detail] (:data response))))

(re-frame/reg-event-fx
  ::fetch-channel-messages
  (fn [_ [_ channel-id]]
    {:http-xhrio {:method :get
                  :uri (str base-url "/channels/" channel-id "/messages")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::set-channel-messages]}}))

(re-frame/reg-event-db
  ::set-channel-messages
  (fn [db [_ response]]
    (assoc-in db [:channel :messages] (:data response))))

(re-frame/reg-event-fx
  ::post-message
  (fn [_ [_ channel-id message on-success on-failure]]
    {:http-xhrio {:method :post
                  :uri (str base-url "/channels/" channel-id "/messages")
                  :params message
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::api-on-success on-success]
                  :on-failure [::api-on-failure on-failure]}}))

(re-frame/reg-event-fx
  ::api-on-success
  (fn [_ [_ callback]]
    {::fx/api-callback {:fn callback}}))

(re-frame/reg-event-fx
  ::api-on-failure
  (fn [_ [_ callback {:keys [response]}]]
    {::fx/api-callback {:fn callback
                        :args [(:errors response)]}}))
