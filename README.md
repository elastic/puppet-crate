# puppet-crate

You love pallet, but sometimes you already have Puppet manifests that
work fine.  Or perhaps you're bootstrapping an environment that you
don't control.  This crate aims to make life easier.

## Usage

Run `install` to get the `puppet` package on the target system.
`setup` to build the remote structure in `/etc/puppet`.  `apply`
executes `puppet apply` on the target system.

    (ns my.servers.puppet
      (:require ...
                [pallet.crate.puppet :as puppet]
                [pallet.api :as pallet]
                ...))
    
    (def puppet-server
      (pallet/server-spec
       :phases {:bootstrap (pallet/plan-fn
                            (puppet/install))
                :configure (pallet/plan-fn
                            (puppet/setup
                             (format "%s/src/puppet-manifests/"
                                     (System/getProperty "user.home"))
                             :conf (io/resource "crate/puppet/puppet.conf"))
                            (puppet/apply))}))

`setup` takes a local path, an optional `:site` with a slurpable
resource (`File`, path `String`, etc.) if you want to override the
`site.pp` in your manifest source directory, and an optional `:conf`
for overriding `/etc/puppet/puppet.conf`.

It's gotten the most development on Ubuntu 12.04 LTS with Puppet 2.7,
but it could work elsewhere.

## License

Copyright © 2013 Elasticsearch
