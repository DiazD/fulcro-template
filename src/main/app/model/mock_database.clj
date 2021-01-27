(ns app.model.mock-database
  "This is a mock database implemented via Datascript, which runs completely in memory, has few deps, and requires
  less setup than Datomic itself.  Its API is very close to Datomics, and for a demo app makes it possible to have the
  *look* of a real back-end without having quite the amount of setup to understand for a beginner."
  (:require
    [datascript.core :as d]
    [mount.core :refer [defstate]]
    [app.util :refer [uuid]]))

;; In datascript just about the only thing that needs schema
;; is lookup refs and entity refs.  You can just wing it on
;; everything else.
(def schema {:account/id {:db/cardinality :db.cardinality/one
                          :db/unique      :db.unique/identity}
             :account/name {:db/cardinality :db.cardinality/one}})

(defn new-database [] (d/create-conn schema))

(defn seed-db! [db]
  (d/transact!
   db
   [
    { :account/id (uuid 1) :account/name "BimBo" :account/email "BimBo@gmail.com" :account/active? true :account/lastname "Bambins"}
    { :account/id (uuid 2) :account/name "Jimmy" :account/email "Jimmy@gmail.com" :account/active? true :account/lastname "ORourke"}
    { :account/id (uuid 3) :account/name "Brown" :account/email "Brown@gmail.com" :account/active? true :account/lastname "Cow"}
    { :account/id (uuid 4) :account/name "Foo" :account/email "Foo@gmail.com" :account/active? true :account/lastname "Bar"}
    { :account/id (uuid 5) :account/name "Kimmy" :account/email "Kimmy@gmail.com" :account/active? true :account/lastname "Mcgee"}]))

(defstate conn :start (new-database))

(comment
  (seed-db! conn)
  )
