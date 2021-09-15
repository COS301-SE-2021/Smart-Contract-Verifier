//This will contain any functions that may need to call Metamask from JS


async function signWithMetamask(data, address) {

	var web3 = window.ethereum;
	var res = await web3.request({ method: 'personal_sign' , params: [data, address]});
	return res;
}
