import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Blockchain/wallet.dart';
import 'package:unison/services/External/jsMethods.dart';
import 'package:unison/services/Server/judgeService.dart';
import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:awesome_loader/awesome_loader.dart';
import 'package:unison/services/Server/loginService.dart';

import '../providers/auth.dart';

class AuthScreen extends StatelessWidget {
  static const routeName = '/auth';

  @override
  Widget build(BuildContext context) {
    final deviceSize = MediaQuery.of(context).size;
    return Scaffold(
      body: Stack(
        children: <Widget>[
          Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: [
                  Color.fromRGBO(32, 32, 46, 1).withOpacity(1),
                  Color.fromRGBO(60, 60, 130, 1).withOpacity(1),
                ],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                stops: [0, 1],
              ),
            ),
          ),
          SingleChildScrollView(
            child: Container(
              height: deviceSize.height,
              width: deviceSize.width,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  SizedBox(
                    height: deviceSize.height * 0.2,
                  ),
                  ShaderMask(
                    shaderCallback: (bounds) => LinearGradient(colors: [
                      Color.fromRGBO(50, 183, 196, 1),
                      Color.fromRGBO(167, 89, 160, 1)
                    ]).createShader(
                      Rect.fromLTWH(0, 0, bounds.width, bounds.height),
                    ),
                    child: Text(
                      'UNISON',
                      style: TextStyle(
                        // The color must be set to white for this to work
                        color: Colors.white,
                        fontSize: 50,
                      ),
                    ),
                  ),
                  SizedBox(
                    height: deviceSize.height * 0.003,
                  ),
                  Flexible(
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        // SizedBox(
                        //   // width: deviceSize.width * 0.4,
                        //   child: TyperAnimatedTextKit(
                        //     speed: Duration(seconds: 1),
                        //     isRepeatingAnimation: false,
                        //     textAlign: TextAlign.center,
                        //     text: [
                        //       "Contracts the smart way.",
                        //     ],
                        //     textStyle: TextStyle(
                        //       fontSize: 16.0,
                        //       color: Colors.white54,
                        //       fontFamily: "Agne",
                        //     ),
                        //   ),
                        // ),
                        SizedBox(
                          // width: 250.0,
                          child: DefaultTextStyle(
                            // textAlign: TextAlign.center,
                            style: const TextStyle(
                              fontSize: 16.0,
                              color: Colors.white54,
                              fontFamily: "Agne",
                            ),
                            child: AnimatedTextKit(
                              animatedTexts: [
                                TyperAnimatedText('Contracts the smart way.',
                                    speed: Duration(milliseconds: 180),
                                    textAlign: TextAlign.center,
                                    curve: Curves.easeInOut),
                              ],
                              isRepeatingAnimation: false,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  SizedBox(
                    height: deviceSize.height * 0.05,
                  ),
                  AuthCard(),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class AuthCard extends StatefulWidget {
  const AuthCard({
    Key key,
  }) : super(key: key);

  @override
  _AuthCardState createState() => _AuthCardState();
}

class _AuthCardState extends State<AuthCard> {
  var _isLoading = false;
  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text('An Error Occurred!'),
        content: Text(message),
        actions: [
          TextButton(
            child: Text('Okay'),
            onPressed: () {
              Navigator.of(ctx).pop();
            },
          ),
        ],
      ),
    );
  }

  Future<void> _submit2() async {
    // JudgeService judgeService = JudgeService();
    // UnisonService unisonService = UnisonService();
     //await await Provider.of<Auth>(context, listen: false).metaMaskLogin();
    // await unisonService.getAgreement(BigInt.from(0));
    // await judgeService.isJudge();
    // //await judgeService.setContractAllowance();

    WalletInteraction wI = WalletInteraction();
    await wI.metamaskConnect();
    LoginService lS = LoginService();

    await lS.login();

  }

  Future<void> _submit() async {
    await addNetwork('Mumbai', 'https://matic-mumbai.chainstacklabs.com', '0x' + 80001.toRadixString(16), 'Matic', 'MATIC');

    JudgeService judgeService = JudgeService();
    UnisonService unisonService = UnisonService();
    setState(() {
      _isLoading = true;
    });

    try {
      await Provider.of<Auth>(context, listen: false).metaMaskLogin();
      //await unisonService.getAgreement(BigInt.from(0));
      await judgeService.isJudge();
      //await judgeService.setContractAllowance();
    } catch (error) {
      _showErrorDialog(error.toString());
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: _isLoading
          ? AwesomeLoader(
              loaderType: AwesomeLoader.AwesomeLoader4,
              color: Color.fromRGBO(50, 183, 196, 0.5),
            )
          : Padding(
              padding:
                  const EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
              child: ElevatedButton(
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Image.asset(
                      'images/metamask-logo-png-transparent.png',
                      height: 24,
                    ),
                    SizedBox(
                      width: 10,
                    ),
                    Text('MetaMask Login'),
                  ],
                ),
                onPressed: _submit,
                style: ButtonStyle(
                  shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  backgroundColor: MaterialStateProperty.all(
                    Color.fromRGBO(50, 183, 196, 0.5),
                  ),
                ),
              ),
            ),
    );
  }
}
