// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'dart:io';
import 'dart:ui';

/// Retrieves current platform locale. US English will be returned if current locale is unrecognisable.
Locale getCurrentLocale() => (() {
      String localeName;
      try {
        localeName = Platform.localeName;
      } catch (e) {
        // Locale is only supported for Android and iOS currently in Sep 2020:
        // importing web library such as dart:html will solve this problem but it would break
        // portability. So we just set default locale to US English here.
        return new Locale("en", "US");
      }

      final localeParts = localeName.split("_");
      switch (localeParts.length) {
        case 1:
          // language only
          return new Locale(localeParts[0]);
        case 2:
          // language + country(ISO 3166) and/or language + script
          final RegExp countryMatcher = new RegExp(r"[A-Z]{2,3}");
          if (countryMatcher.hasMatch(localeParts[1])) {
            return new Locale(localeParts[0], localeParts[1]);
          } else {
            return Locale.fromSubtags(languageCode: localeParts[0], scriptCode: localeParts[1]);
          }
          break;
        case 3:
          return Locale.fromSubtags(
              languageCode: localeParts[0], scriptCode: localeParts[1], countryCode: localeParts[2]);
      }
    }());
