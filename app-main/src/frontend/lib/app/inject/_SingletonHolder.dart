// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'package:flutter/foundation.dart';

/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 07 - Sep - 2020
class SingletonHolder {
  // Assume all instances in module definition are singleton and accessed on a single thread
  final instances = Map();

  /// Use this method if you are too lazy to specify a qualified name of `<T>`.
  /// Don't use this for types of `<T>` that are well-known, and/or likely to be referenced many times.
  @nonVirtual
  T getInstanceOf<T>(T instanceCreator()) {
    return getInstanceOf2("", instanceCreator);
  }

  @nonVirtual
  T getInstanceOf2<T>(final String qualifier, T instanceCreator()) {
    final String instanceName = (() {
      if (qualifier.isEmpty) {
        return "${this.runtimeType}__${_typeOf<T>()}";
      } else {
        return "${this.runtimeType}__${_typeOf<T>()}__$qualifier";
      }
    }());

    if (instances[instanceName] == null) {
      instances[instanceName] = instanceCreator();
    }

    return instances[instanceName] as T;
  }

  @nonVirtual
  Type _typeOf<T>() => T;
}
