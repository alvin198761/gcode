package org.alvin.home.v3.code.system.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocField {
    private String column_name;
    private String column_comment;
    private String column_type;
    private String is_nullable;
    private String pri;
}