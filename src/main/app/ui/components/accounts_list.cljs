(ns app.ui.components.accounts-list
  (:require
   [com.fulcrologic.fulcro.components :as prim :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [com.fulcrologic.fulcro.mutations :as m]
   [com.fulcrologic.fulcro.algorithms.form-state :as fs]
   [app.ui.forms.utils :as fu]))


(defsc AccountItemForm
  "User Account Edit Form"
  [this {:account/keys [id name email active? editing?] :as props}]
  {:query [:account/editing? :account/id :account/name :account/email :account/active? fs/form-config-join]
   :ident :account/id
   :form-fields #{:account/name :account/email :account/active?}}
  (let [form-dirty? (fs/dirty? (prim/props this))]
    (println "Form DIrty?" form-dirty? (fs/dirty? props))
    (dom/tr :.ui.form {:classes [(when form-dirty? "warning")]}
            (dom/td (dom/input {:value name
                                :name :account/name
                                :onChange #(m/set-string! this :account/name :event %)}))
            (dom/td (dom/input {:value email
                                :name :account/email
                                :type "email"
                                :onChange #(m/set-string! this :account/email :event %)}))
            (dom/td (dom/input {:checked active? :name :account/active? :type "checkbox"}))
            (dom/td (dom/div
                     (dom/button {:onClick
                                  #(prim/transact! this `[(app.model.account/submit-account-changes {:account-id ~id :delta ~(fs/dirty-fields props true)})])}
                                 "Save")
                     (dom/button {:onClick
                                  #(prim/transact! this `[(app.model.account/cancel-account-edit {:account-id ~id})])}
                                 "Cancel"))))))

(def account-form (prim/factory AccountItemForm {:keyFn :account/id}))

(defsc AccountListItem
  "An account list item"
  [this {:account/keys [id name email active? editing?] :as props}]
  {:query [:account/id :account/name :account/email :account/active?
           :account/editing?]
   :ident :account/id}
  (if editing?
    (account-form props)
    (dom/tr
     (dom/td name)
     (dom/td email)
     (dom/td
      (dom/div :.ui.label.green
               (if active? "Active" "Inactive")))
     (dom/td
      (dom/button {:onClick
                   (fn []
                     (prim/transact! this `[(app.model.account/edit-account {:account-id ~id})]))}
                  "Edit")))))

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
