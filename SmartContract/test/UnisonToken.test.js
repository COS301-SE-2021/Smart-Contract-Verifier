const { assert } = require('chai')

const UnisonToken = artifacts.require("UnisonToken")
const {advanceTimeAndBlock} = require("./helper.js")

require('chai').use(require('chai-as-promised')).should()

const c = BigInt("92592592592592592");
const m = BigInt("1071673525");

function mintedAtTime(x){
    x = BigInt(x);

    x += BigInt("86400");
    if(x > BigInt(86400 * 500))
        x = BigInt(86400 * 500);

    result = c * x - m*x*x;
    return BigInt(result);
}

async function getCurrBlockTime(){
    var block = await web3.eth.getBlock('latest');
    var result = BigInt(block.timestamp);
    console.log("Block time: " + result);
    return result;
}


contract('UnisonToken', (accounts) =>{

    describe("UnisonToken general tests", async () =>{

        let token

        before(async () =>{
            token = await UnisonToken.new()
        })

        it("Transfer token", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that both balances are updated accurately

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)
            token.transfer(accounts[1], transferSize, {from: accounts[0]});

            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After + transferSize);
            assert.equal(acc1Before + transferSize, acc1After);

        })


        it("Total supply", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that the totalSupply doesn't change
            
            var supplyBefore = await token.totalSupply();
            supplyBefore = BigInt(supplyBefore);


            var transferSize = BigInt(1000)
            token.transfer(accounts[1], transferSize, {from: accounts[0]});

            var supplyAfter = await token.totalSupply();
            supplyAfter = BigInt(supplyAfter);

            assert.equal(supplyBefore, supplyAfter);

        })

        it("Can't use transferFrom w/o allowance", async() =>{
            // Attempts to transfer a set amount of token from account 0 to account 1
            // without an allowance, neither balance should change

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)

            try{
                await token.transferFrom(accounts[0], accounts[1], transferSize, {from: accounts[2]});
            }
            catch(error){}


            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After);
            assert.equal(acc1Before, acc1After);

        })

        it("Can use transferFrom with allowance", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that both balances are updated accurately

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)

            try{
                await token.approve(accounts[2], transferSize, {from: accounts[0]});
                await token.transferFrom(accounts[0], accounts[1], transferSize, {from: accounts[2]});
            }
            catch(error){}


            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After + transferSize);
            assert.equal(acc1Before + transferSize, acc1After);

        })


        it("Can decrease allowance", async() =>{
            // decreaseAllowance should reduce allowance by a specific amount

            var allowBefore = await token.allowance(accounts[0], accounts[1]);
            allowBefore = BigInt(allowBefore);

            var up = BigInt(1000)
            var down = BigInt(400)

            try{
                await token.approve(accounts[1], up, {from: accounts[0]});
                await token.decreaseAllowance(accounts[1], down, {from: accounts[0]})
            }
            catch(error){}


            var allowAfter = await token.allowance(accounts[0], accounts[1]);
            allowAfter = BigInt(allowAfter);

            assert.equal(allowAfter - allowBefore, up - down);

        })


        it("approve sets allowance properly", async() =>{
            // Makes changes to allowance and then sets it to a specific amount

            var allowBefore = await token.allowance(accounts[0], accounts[1]);
            allowBefore = BigInt(allowBefore);

            var up = BigInt(1000)
            var down = BigInt(400)
            var finalAmount = BigInt(25565)

            try{

                await token.increaseAllowance(accounts[1], up, {from: accounts[0]});
                await token.decreaseAllowance(accounts[1], down, {from: accounts[0]});
                await token.approve(accounts[1], finalAmount, {from: accounts[0]});
            }
            catch(error){}


            var allowAfter = await token.allowance(accounts[0], accounts[1]);
            allowAfter = BigInt(allowAfter);

            assert.equal(allowAfter, finalAmount);

        })

    })


    describe("UnisonToken minting unit tests", async () =>{

        let token
        let deployTime;

        async function assertSupply(){
            var currTime = await getCurrBlockTime();
            token.receiveMinted();
            var supply = await token.totalSupply();
            supply = BigInt(supply);

            var expectedSupply = mintedAtTime(currTime - deployTime);

            assert(supply == expectedSupply, "Incorrect balance " + supply + ", expected " + expectedSupply);
        }

        before(async () =>{
            deployTime = await getCurrBlockTime();

            token = await UnisonToken.new();
        })

        it("Initial amount is correct", async ()=>{
            var acc0Start = await token.totalSupply();
            acc0Start = BigInt(acc0Start);
            assert(acc0Start == mintedAtTime(0), "Initial amount is incorrect");
        })

        it("Testing time passage helper function", async ()=>{
          
            var originalBlock = await web3.eth.getBlock('latest');
            var newBlock = await advanceTimeAndBlock(1000);
            var timeDiff = newBlock.timestamp - originalBlock.timestamp;

            assert(timeDiff >= 1000, "Block time wasn't adjusted");
        })

        it("balance after some time passed", async ()=>{
            await assertSupply();
        })

        it("balance after more time passed", async ()=>{
            await advanceTimeAndBlock(15);
            await assertSupply();
        })

        it("balance about halfway through", async ()=>{
            await advanceTimeAndBlock(250 * 86400);
            await assertSupply();
        })

        it("balance after 500 days", async ()=>{
            await advanceTimeAndBlock(250 * 86400);
            await assertSupply();
        })

        it("balance some time after minting ended", async ()=>{
            await advanceTimeAndBlock(50 * 86400);
            await assertSupply();
        })

        it("Can't access receiveMinted from oter account", async()=>{
            try{
                token.receiveMinted({from:accounts[1]});
                assert(false, "onlyMinter modifier didn't work");
            }
            catch{}
        })

    })

})