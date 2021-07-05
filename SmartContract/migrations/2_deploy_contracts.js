const Verifier = artifacts.require("Verifier");
const UnisonToken = artifacts.require("UnisonToken");

module.exports = async (deployer) => {
  deployer.deploy(UnisonToken).then(function(){
    return deployer.deploy(Verifier, UnisonToken.address);
  })


};
