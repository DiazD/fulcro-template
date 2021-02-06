(ns app.ui.components.accounts-list
  (:require
   [com.fulcrologic.fulcro.components :as prim :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.data-fetch :as df]))

(defsc AccountListItem
  "An account list item"
  [this {:account/keys [id name email active?] :as props}]
  {:query [:account/id :account/name :account/email :account/active?]
   :ident :account/id
   :initLocalState (fn [this _] {:editing? false})}
  (let [editing? (prim/get-state this :editing?)]
    (if editing?
      (dom/tr
       (dom/td (dom/input {:name :account/name}))
       (dom/td (dom/input {:name :account/email :type "email"}))
       (dom/td (dom/input {:name :account/active? :type "checkbox"}))
       (dom/td (dom/div
                (dom/button {:onClick
                             #(prim/set-state!
                               this
                               {:editing? (not editing?)})}
                            "Save")
                (dom/button {:onClick
                             #(prim/set-state!
                               this
                               {:editing? (not editing?)})}
                            "Cancel"))))
      (dom/tr
       (dom/td name)
       (dom/td email)
       (dom/td
        (dom/div :.ui.label.green
                 (if active? "Active" "Inactive")))
       (dom/td
        (dom/button {:onClick
                     #(prim/set-state!
                       this
                       {:editing? (not editing?)})}
                    "Edit"))))))

(def account-item (prim/factory AccountListItem {:keyfn :account/id}))

(defsc AccountsList
  "Show all of our accounts"
  [this {:list/keys [accounts]}]
  {:query [:list/id {:list/accounts (prim/get-query AccountListItem)}]
   :initial-state {:list/id :param/id :list/accounts []}
   :ident :list/id}
  (dom/table
   :.ui.fixed.single.line.celled.table
   (dom/thead
    (dom/tr
     (dom/th "Name")
     (dom/th "Email")
     (dom/th "Status")
     (dom/th "Action")))
   (dom/tbody
    (map account-item accounts))))

(def accounts-list (prim/factory AccountsList))
