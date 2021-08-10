const Verifier = artifacts.require("Verifier");
const UnisonToken = artifacts.require("UnisonToken");
const RandomSource = artifacts.require("Utilities/RandomSource.sol");

fs = require('fs');

module.exports = async (deployer) => {
  await deployer.deploy(RandomSource);
  await deployer.deploy(UnisonToken);
  await deployer.deploy(Verifier, UnisonToken.address, RandomSource.address);


  var obj =  [];
  obj.push({name : "Verifier", address : Verifier.address});
  obj.push({name : "UnisonToken", address : UnisonToken.address});

  // This file will allow front end to know where the smart contracts have been deployed
  fs.writeFile("../frontend_scv/assets/JSON/address.json", JSON.stringify(obj), function(err){
    console.log(err);
  })
};
