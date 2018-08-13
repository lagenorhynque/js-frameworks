(ns re-frame-sample-app.events.channel
  (:require [re-frame-sample-app.events.api :as api]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::fetch-detail
  (fn [_ [_ channel-id]]
    {:http-xhrio (assoc (api/get-channel channel-id)
                        :on-success [::on-success-fetch-detail])}))

(re-frame/reg-event-db
  ::on-success-fetch-detail
  (fn [db [_ response]]
    (assoc-in db [:channel :detail] (:data response))))

(re-frame/reg-event-fx
  ::fetch-messages
  (fn [_ [_ channel-id]]
    {:http-xhrio (assoc (api/get-channel-messages channel-id)
                        :on-success [::on-success-fetch-messages])}))

(re-frame/reg-event-db
  ::on-success-fetch-messages
  (fn [db [_ response]]
    (assoc-in db [:channel :messages] (:data response))))

(re-frame/reg-event-db
  ::update-message-form
  (fn [db [_ k v]]
    (assoc-in db [:channel :message-form k] v)))

(re-frame/reg-event-db
  ::clear-message-form
  (fn [db _]
    (assoc-in db [:channel :message-form] {:body ""})))

(re-frame/reg-event-fx
  ::post-message
  (fn [_ [_ channel-id message on-success on-failure]]
    {:http-xhrio (assoc (api/post-channel-message channel-id (assoc message
                                                                    :user_id 1))
                        :on-success [::on-success-post-message channel-id]
                        :on-failure [::on-failure-post-message])}))

(re-frame/reg-event-fx
  ::on-success-post-message
  (fn [_ [_ channel-id]]
    {:dispatch-n [[::clear-message-form]
                  [::fetch-messages channel-id]]}))

(re-frame/reg-event-fx
  ::on-failure-post-message
  (fn [_ [_ {:keys [response]}]]
    {:dispatch [::update-message-form
                :errors (:errors response)]}))
