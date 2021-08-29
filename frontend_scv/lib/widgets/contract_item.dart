import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../models/contract.dart';
import '../providers/auth.dart';
import '../screens/view_contract_screen.dart';

class ContractItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final contract = Provider.of<Contract>(context);
    final String rawSvg = Jdenticon.toSvg(
      contract.contractId,
      colorSaturation: 1.0,
      grayscaleSaturation: 1.0,
      colorLightnessMinValue: 0.40,
      colorLightnessMaxValue: 0.80,
      grayscaleLightnessMinValue: 0.30,
      grayscaleLightnessMaxValue: 0.90,
      backColor: '#ff7800',
      hues: [205],
    );

    /*
    * Hues = new HueCollection { { 191, HueUnit.Degrees } },
            BackColor = Color.FromRgba(255, 130, 0, 255),
            ColorLightness = Range.Create(0.29f, 0.60f),
            GrayscaleLightness = Range.Create(0.53f, 0.86f),
            ColorSaturation = 1.00f,
            GrayscaleSaturation = 1.00f
    * */

    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewContractScreen.routeName,
          arguments: contract.contractId,
        );
      },
      child: ListTile(
        title: Text(contract.title),
        leading:
            // CircleAvatar(
            //   backgroundImage: SvgPicture.
            SvgPicture.string(
          rawSvg,
          fit: BoxFit.fill,
          height: 32,
          width: 32,
        ),
      ),
    );
  }
}
