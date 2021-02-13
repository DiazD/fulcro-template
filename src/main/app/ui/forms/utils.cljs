(ns app.ui.forms.utils
  (:require [com.fulcrologic.fulcro.components :as cp]
            [com.fulcrologic.fulcro.algorithms.form-state :as fs]))

(defn field-attrs
  "A helper function for getting aspects of a particular field."
  [component field]
  (let [form         (cp/props component)
        entity-ident (cp/get-ident component form)
        id           (str (first entity-ident) "-" (second entity-ident))
        is-dirty?    (fs/dirty? form field)
        clean?       (not is-dirty?)
        validity     (fs/get-spec-validity form field)
        is-invalid?  (= :invalid validity)
        value        (get form field "")]
    {:dirty?   is-dirty?
     :ident    entity-ident
     :id       id
     :clean?   clean?
     :validity validity
     :invalid? is-invalid?
     :value    value}))
