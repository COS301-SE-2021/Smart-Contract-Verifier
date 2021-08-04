module.exports.giveJurorsCoins = (token, source, destinations, amountEach) =>{
    for(var i=0; i<destinations.length; i++){
        token.transfer(destinations[i], amountEach, {from:source});
    }
}
