(ns re-frame-sample-app.events.channel
  (:require [re-frame-sample-app.events.api :as api]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::fetch-detail
  (fn [_ [_ channel-id]]
    {:http-xhrio (assoc (api/get-channel channel-id)
                        :on-success [::set-detail])}))

(re-frame/reg-event-db
  ::set-detail
  (fn [db [_ response]]
    (assoc-in db [:channel :detail] (:data response))))

(re-frame/reg-event-fx
  ::fetch-messages
  (fn [_ [_ channel-id]]
    {:http-xhrio (assoc (api/get-channel-messages channel-id)
                        :on-success [::set-messages])}))

(re-frame/reg-event-db
  ::set-messages
  (fn [db [_ response]]
    (assoc-in db [:channel :messages] (:data response))))

(re-frame/reg-event-fx
  ::post-message
  (fn [_ [_ channel-id message on-success on-failure]]
    {:http-xhrio (assoc (api/post-channel-message channel-id message)
                        :on-success [::api/on-success on-success]
                        :on-failure [::api/on-failure on-failure])}))
