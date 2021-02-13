(ns app.model.account
  (:require
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.algorithms.data-targeting :as targeting]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [app.ui.components.accounts-list :refer [AccountItemForm]]))

;; form-state mutation
(defmutation edit-account [{:keys [account-id]}]
  (action [{:keys [state]}]
          (swap! state
                 (fn [s]
                   (-> s
                       (fs/add-form-config*
                        AccountItemForm
                        [:account/id account-id])
                       (fs/pristine->entity* [:account/id account-id])
                       (fs/mark-complete* [:account/id account-id])
                       (assoc-in [:account/id account-id :account/editing?] true))))))

(defmutation cancel-account-edit [{:keys [account-id]}]
  (action [{:keys [state]}]
          (swap! state
                 (fn [s]
                   (-> s
                       (assoc-in [:account/id account-id :account/editing?] false)
                       (fs/pristine->entity* [:account/id account-id]))))))

;; TODO: Make a mutation for submission
;; currently doesn't work -- dirty-fields are returning empty-map
(defmutation submit-account-changes [{:keys [account-id delta]}]
  (action [{:keys [state]}]
          (swap! state
                 (fn [s]
                   (-> s
                       (assoc-in [:account/id account-id :account/editing?] false)
                       (fs/pristine->entity* [:account/id account-id])))))
  (remote [env] true)
  (refresh [env] [:account/id account-id]))
