(ns re-frame-sample-app.views.channel-list
  (:require [cljs-react-material-ui.icons :as ic]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-sample-app.events :as events]
            [re-frame-sample-app.subs :as subs]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]))

(defn channel-list []
  [ui/drawer {:open true}
   [ui/list
    [ui/list-item
     {:right-icon (ic/action-home)
      :primary-text "Home"
      :on-click #(re-frame/dispatch [::events/navigate
                                     :re-frame-sample-app.views.home/view])}]
    [ui/list-item
     {:right-icon (ic/action-list)
      :primary-text "Channels"
      :disabled true
      :open true
      :nested-items (reagent/as-element
                     (map (fn [channel]
                            [ui/list-item
                             {:key (:id channel)
                              :primary-text (:name channel)
                              :on-click #(re-frame/dispatch [::events/navigate
                                                             :re-frame-sample-app.views.channel/view
                                                             {:channel-id (:id channel)}])}])
                          @(re-frame/subscribe [::subs/channels])))}]]])
