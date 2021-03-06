(ns re-frame-sample-app.events.channel-list
  (:require [re-frame-sample-app.events.api :as api]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::fetch-channels
  (fn [_ _]
    {:http-xhrio (assoc api/get-channels
                        :on-success [::on-success-fetch-channels])}))

(re-frame/reg-event-db
  ::on-success-fetch-channels
  (fn [db [_ response]]
    (assoc db :channels (:data response))))
