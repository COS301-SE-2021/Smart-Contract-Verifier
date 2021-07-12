const Verifier = artifacts.require("Verifier");
const UnisonToken = artifacts.require("UnisonToken");
const RandomSource = artifacts.require("Randomness/RandomSource.sol");

module.exports = async (deployer) => {
  await deployer.deploy(RandomSource);
  await deployer.deploy(UnisonToken);
  await deployer.deploy(Verifier, UnisonToken.address, RandomSource.address);

  // deployer.deploy(UnisonToken).then(function(){
  //   return deployer.deploy(Verifier, UnisonToken.address, RandomSource.address);
  // })
};
