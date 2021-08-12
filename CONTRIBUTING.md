# Coding standards

## Git structure
The structure of the repository is as follows:

```
master
  dev
    dev_subsystem
      dev_subsystem_feature
```

We are using the git flow methodology. master must always contain a working version of our code and will likely only be updated once per demo. The dev is where the different subsystems are combined and is branched from master. Every subsystem has its own development branch, which is branched from dev. The naming convention for these branches, as specified above, is dev\_subsystem where subsystem is one of the following: \{smart, server, front\}.

Note that these are not the same subsystem divisions from the SRS document, since these were divided from an architectural point of view as opposed to a functional one. 

Every subsystem can have any number of feature branches, identified by appending \_feature to the name. The feature name should be short, but descriptive.  Use camelCase for feature names (no spaces and every word except the first one starts with a capital letter).

An Example:

```    
master
  dev
    dev_smart
      dev_smart_token
    dev_front
      dev_front_metamask
      dev_front_newButton
```

If it ever becomes necessary, we can subdivide subsystems even further. We can do this by adding dev\_subsystem\_subsubsystem and dev\_subsystem\_subsubsystem\_feature branches. It is unlikely that we will need to do this, but the option is available.

## File structure
Every subsystem has its own has its own folder. Code is therefore grouped by subsystem. The basic layout is as follows:

```
    frontend_scv
    Smart-Contract-Verifier-Server
    SmartContract
        src
        test
```

## Code standards

**Styling rules**

- **Use camel case** Class names must start with a capital letter, but attributes, objects and variables must start with lower case. i.e. thisIsAnObject, ThisIsAClass. 
- **Enum standard:** The enum class is in camel case, starting with a capital letter. The possible values for the enum are in all caps.

**General guidelines**

- **Keep your code readable.** If it isn't simple and self explanatory, add comments.
- **Keep your code maintainable and modular**, with the exception of the smart contract since it will be "set in stone" once deployed. It should be easy to swap components of our code, for example switching to a different persistence technology. The effective use of interfaces should help fulfill this requirement. 
- **Write efficient code.** Pay attention to the big O notations of any algorithms you use. If you write code with exponential complexity for example, you are probably doing something wrong.

## Code reviews
Every pull request made to the dev branch must be reviewed by someone other than the person/people who wrote the code. The goal of this review is to ensure that the code standards specified above have been followed. If there are any deviations, changes must be requested to fix them.

We will also do a broader code review every few weeks (likely once per demo) to decide if we're happy with the state of our work. These reviews will be more integration-oriented, to determine if our architecture is 1) properly followed and 2) still a good choice. This review will focus mainly on how different components communicate and on how useful each component is.
