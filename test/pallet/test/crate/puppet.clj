(ns pallet.test.crate.puppet
  (:require [clojure.test :refer :all]
            [pallet.build-actions :refer [build-actions build-session]]
            [pallet.crate.puppet :as puppet]))

(deftest puppet
  (is (first
       (build-actions {}
         (puppet/install)
         (puppet/setup "foooo")))))
