(ns pallet.crate.puppet
  (:refer-clojure :exclude [apply])
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [pallet.action :refer [defaction implement-action]]
            [pallet.actions :refer [package rsync-directory
                                    directory remote-file]]
            [pallet.crate :as crate]
            [pallet.stevedore :as stevedore]))

(def module-path "puppet/modules")

(defaction puppet-apply*
  "Apply a puppet module."
  [])

(implement-action puppet-apply* :direct
  {:action-type :script :location :target}
  [session]
  (let [cmd "sudo puppet apply --color=false /etc/puppet/manifests/site.pp"]
    [[{:language :bash}
      (stevedore/checked-commands (format "puppet: %s" cmd) cmd)]
     session]))

(defn puppet-apply
  "Run puppet on the listed modules."
  {:pallet/plan-fn true}
  []
  (crate/phase-context
   puppet-apply-fn {:name puppet-apply}
   (puppet-apply*)))

(defn install []
  (package "rsync")
  (package "puppet"))

(defn setup
  "Deploy a local path of Puppet manifests to target nodes in
  /etc/puppet.  Optionally provide a slurpable :conf (puppet.conf)
  or :site (site.pp), although those can be provided in the manifest
  directory structure represented by the first argument."
  [manifests & {:keys [conf site]}]
  (log/debug "sync" manifests)
  (directory
   "/etc/puppet"
   :action :create
   :owner (:username (crate/admin-user)))
  (rsync-directory manifests "/etc/puppet/" :exclude ".git")
  (directory
   "/etc/puppet"
   :action :touch
   :owner "root" :group "root")
  (when conf
    (remote-file
     "/etc/puppet/puppet.conf"
     :content (slurp conf)
     :owner "root"
     :group "root"))
  (when site
    (remote-file
     "/etc/puppet/manifests/site.pp"
     :content (slurp site)
     :owner "root"
     :group "root"
     :overwrite-changes true)))

(defn apply []
  (log/info "apply!")
  (puppet-apply))
