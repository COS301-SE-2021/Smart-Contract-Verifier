import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/services/Blockchain/faucetService.dart';
import 'package:unison/services/Blockchain/tokenService.dart';

class DashBoardActions extends StatelessWidget {
  final TokenService tokServ = TokenService();
  final FaucetService _faucetService = FaucetService();

  @override
  Widget build(BuildContext context) {
    Widget _spacer = SizedBox(
      width: MediaQuery.of(context).size.width * 0.05,
    );

    Widget _refreshButton = TextButton(
      style: ButtonStyle(
        foregroundColor: MaterialStateProperty.all(
          Color.fromRGBO(50, 183, 196, 0.8),
        ),
      ),
      child: Row(
        children: [
          Icon(Icons.refresh),
          Text('Refresh Dashboard'),
        ],
      ),
      onPressed: () async {
        await Navigator.of(context).pushNamed('/');
      },
    );

    return FutureBuilder(
      future: tokServ.getAllowance(),
      builder: (context, snap) {
        if (snap.connectionState == ConnectionState.done) {
          return Row(
            children: [
              _refreshButton, //Refresh
              _spacer,
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      FutureBuilder(
                        future: tokServ.getBalance(),
                        builder: (context, snapshot) {
                          return Text(
                            'UNT Balance: ' + snapshot.data.toString(),
                            style: TextStyle(color: Colors.white70),
                          );
                        },
                      ),
                      TextButton(
                        style: ButtonStyle(
                          foregroundColor: MaterialStateProperty.all(
                            Color.fromRGBO(182, 80, 158, 0.8),
                          ),
                        ),
                        child: Row(
                          children: [
                            _spacer,
                            Text(
                              'Get UNT from Faucet',
                              style: TextStyle(
                                decoration: TextDecoration.underline,
                              ),
                            ),
                            SizedBox(
                              width: 2,
                            ),
                            Icon(
                              Icons.point_of_sale_outlined,
                            ),
                          ],
                        ),
                        onPressed: () async {
                          await _faucetService.getToken();
                        },
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Token allowance: ' + snap.data.toString(),
                        style: TextStyle(color: Colors.white70),
                      ),
                      TextButton(
                        style: ButtonStyle(
                          foregroundColor: MaterialStateProperty.all(
                            Color.fromRGBO(182, 80, 158, 0.8),
                          ),
                        ),
                        child: Row(
                          children: [
                            _spacer,
                            Text(
                              'Top Up',
                              style: TextStyle(
                                decoration: TextDecoration.underline,
                              ),
                            ),
                            SizedBox(
                              width: 2,
                            ),
                            Icon(
                              Icons.price_change_outlined,
                            ),
                          ],
                        ),
                        onPressed: () async {
                          await tokServ
                              .setAllowance(BigInt.from(2000000000000000000));
                        },
                      ),
                    ],
                  ),
                ],
              ),
              _spacer,
            ],
          );
        }
        return AwesomeLoader(
          color: Color.fromRGBO(182, 80, 158, 0.8),
          loaderType: AwesomeLoader.AwesomeLoader4,
        );
      },
    );
  }
}
