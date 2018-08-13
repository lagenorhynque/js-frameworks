(ns re-frame-sample-app.views.channel
  (:require [cljs-react-material-ui.reagent :as ui]
            [re-frame-sample-app.events.channel :as events.channel]
            [re-frame-sample-app.subs.channel :as subs.channel]
            [re-frame-sample-app.views.core :refer [init view]]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [stylefy.core :refer [use-style]]))

(defn message-feed [{:keys [channel-id]}]
  (let [detail @(re-frame/subscribe [::subs.channel/detail])
        messages @(re-frame/subscribe [::subs.channel/messages])]
    [ui/list
     [ui/subheader (:name detail)]
     (map (fn [message]
            [ui/list-item
             {:key (:id message)
              :left-avatar (reagent/as-element [ui/avatar])
              :primary-text (:user_name message)
              :secondary-text (:body message)}])
          (reverse messages))]))

(def message-form-style
  {:padding "30px"
   :display "flex"
   :flex-direction "column"
   :align-items "center"})

(defn message-form [{:keys [channel-id]}]
  (let [message @(re-frame/subscribe [::subs.channel/message-form])]
    (letfn [(handle-text-area-change [e]
              (.preventDefault e)
              (re-frame/dispatch [::events.channel/update-message-form
                                  :body (-> e .-currentTarget .-value)]))
            (handle-form-submit [e]
              (.preventDefault e)
              (re-frame/dispatch [::events.channel/post-message channel-id message]))]
      [ui/paper (use-style message-form-style)
       [ui/text-field
        {:multi-line true
         :full-width true
         :hint-text "Write your message"
         :value (:body message)
         :on-change handle-text-area-change}]
       [ui/raised-button
        {:label "Send"
         :primary true
         :on-click handle-form-submit}]])))

(defmethod init ::view [{:keys [route-params]}]
  (let [channel-id (:channel-id route-params)]
    (re-frame/dispatch [::events.channel/fetch-detail channel-id])
    (re-frame/dispatch [::events.channel/fetch-messages channel-id])))

(defmethod view ::view [{:keys [route-params]}]
  (let [channel-id (:channel-id route-params)]
    [:div
     [message-feed
      {:channel-id channel-id}]
     [message-form
      {:channel-id channel-id}]]))
