(ns re-frame-sample-app.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-frame-sample-app.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
