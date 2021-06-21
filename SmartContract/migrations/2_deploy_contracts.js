const Verifier = artifacts.require("Verifier");
const AgreeToken = artifacts.require("AgreeToken");

module.exports = function (deployer) {
  deployer.deploy(Verifier);
  deployer.deploy(AgreeToken);

};
