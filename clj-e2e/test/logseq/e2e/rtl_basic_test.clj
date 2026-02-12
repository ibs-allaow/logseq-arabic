(ns logseq.e2e.rtl-basic-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [logseq.e2e.assert :as assert]
            [logseq.e2e.fixtures :as fixtures]
            [logseq.e2e.keyboard :as k]
            [logseq.e2e.settings :as settings]
            [logseq.e2e.util :as util]
            [wally.main :as w]))

(use-fixtures :once fixtures/open-page)

(use-fixtures :each
  fixtures/new-logseq-page
  fixtures/validate-graph)

(defn- sidebar-width []
  (Double/parseDouble (w/eval-js "String(document.querySelector('#right-sidebar')?.getBoundingClientRect().width || 0)")))

(deftest rtl-language-switch-smoke
  (testing "switch language to arabic updates html/app direction state"
    (settings/set-language! "ar")
    (is (= "ar" (w/eval-js "document.documentElement.getAttribute('lang')")))
    (is (= "rtl" (w/eval-js "document.documentElement.getAttribute('dir')")))
    (is (= "rtl" (w/eval-js "document.querySelector('#app-container-wrapper')?.getAttribute('data-dir')"))))

  (testing "sidebars expose rtl-aware visual handles"
    (util/search-and-click "Toggle right sidebar")
    (assert/assert-is-visible "#right-sidebar.open")
    (assert/assert-is-visible ".cp__right-sidebar .resizer[data-side='east']")
    (assert/assert-is-visible "#left-sidebar .left-sidebar-resizer[data-side='left']"))

  (testing "right sidebar keyboard arrows resize in rtl"
    (w/click ".cp__right-sidebar .resizer[data-side='east']")
    (let [before (sidebar-width)]
      (k/arrow-left)
      (util/wait-timeout 80)
      (let [after-left (sidebar-width)]
        (k/arrow-right)
        (util/wait-timeout 80)
        (let [after-right (sidebar-width)]
          (is (not= before after-left))
          (is (not= after-left after-right)))))))
