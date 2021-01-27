(ns app.ui.components.accounts-list
  (:require
   [com.fulcrologic.fulcro.components :as prim :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.data-fetch :as df]))

(defsc AccountListItem
  "An account list item"
  [this {:account/keys [id name email active?]}]
  {:query [:account/id :account/name :account/email :account/active?]
   :ident :account/id}
  (dom/li email))

(def account-item (prim/factory AccountListItem {:keyfn :account/id}))

(defsc AccountsList
  "Show all of our accounts"
  [this {:list/keys [accounts]}]
  {:query [:list/id {:list/accounts (prim/get-query AccountListItem)}]
   :initial-state {:list/id :param/id :list/accounts []}
   :ident :list/id}
  (dom/div
    (map account-item accounts)))

(def accounts-list (prim/factory AccountsList))
