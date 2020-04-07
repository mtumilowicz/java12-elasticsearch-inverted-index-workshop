package document;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class DocumentId {

   private final String id;

   public static DocumentId of(String id) {
      return new DocumentId(id);
   }
}
