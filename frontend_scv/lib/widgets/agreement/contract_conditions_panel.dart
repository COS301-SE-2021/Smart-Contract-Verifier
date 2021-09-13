import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:flutter/material.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/agreement/condition_item.dart';
import '../../models/contract.dart';

class ContractConditionsPanel extends StatelessWidget {
  final Contract _contract;
  final Function _reloadMyParent;
  ContractConditionsPanel(this._contract, this._reloadMyParent);
  @override
  Widget build(BuildContext context) {
    NegotiationService _negotiationService = NegotiationService();
    return Expanded(
      child: Container(
        child: _contract.conditions.isEmpty //TODO handle empty conditions
            ? Padding(
                padding: const EdgeInsets.all(8.0),
                child: SizedBox(
                  child: DefaultTextStyle(
                    style: const TextStyle(
                      fontSize: 14,
                      color: Colors.pinkAccent,
                      shadows: [
                        Shadow(
                          blurRadius: 7.0,
                          color: Colors.pinkAccent,
                          offset: Offset(0, 0),
                        ),
                      ],
                    ),
                    child: AnimatedTextKit(
                      repeatForever: true,
                      animatedTexts: [
                        FlickerAnimatedText(
                          'No Conditions Set',
                          textAlign: TextAlign.center,
                        ),
                        FlickerAnimatedText(
                          'You can add conditions above',
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
              )
            : ListView.builder(
                itemCount: _contract.conditions.length,
                itemBuilder: (_, i) => Column(
                  children: [
                    // Text(_contract.conditions[i].description),
                    ConditionItem(
                      contractCondition: _contract.conditions[i],
                      negotiationService: _negotiationService,
                      reloadParent: _reloadMyParent,
                      paymentId: _contract.paymentID,
                      durationId: _contract.durationID,
                    ),
                    Divider(),
                  ],
                ),
              ),
      ),
    );
  }
}
