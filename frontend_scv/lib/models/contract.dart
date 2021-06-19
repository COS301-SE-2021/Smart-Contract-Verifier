import 'package:flutter/foundation.dart';

enum TermStatus { Accepted, Rejected, Pending }
enum ContractStatus {
  Negotiation,
  Sealed,
}

class Term {
  final String id;
  final String text;
  final String description;
  final TermStatus status;
  final String partyId;

  Term.name(this.id, this.text, this.description, this.status, this.partyId);
}

class Contract {
  final String id;
  final List<Term> Terms;
  final ContractStatus status;

  Contract.name(this.id, this.Terms, this.status);
}
