package document;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class DocumentId {

   private final String id;

   public static DocumentId of(String id) {
      return new DocumentId(id);
   }
}
