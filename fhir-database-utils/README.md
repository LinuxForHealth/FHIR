Expression Precedence Levels

| Precedence Level | Expression Node |
| ---------------- | --------------- |
| 0 | LiteralExpNode |
| 0 | ParenExpNode |
| 0 | ColumnExpNode |
| 0 | BindMarkerNode |
| 1 | LeftParenExpNode |
| 1 | ExistsExpNode |
| 1 | RightParenExpNode |
| 1 | NotExistsExpNode |
| 2 | MultiplicativeExpNode |
| 3 | AdditiveExpNode |
| 4 | ComparativeExpNode |
| 4 | EqualityExpNode |
| 5 | SinExpNode |
| 5 | CosExpNode |
| 5 | ACosExpNode |
| 5 | CoalesceExpNode |
| 5 | InListExpNode |
| 5 | IsNotNullExpNode |
| 5 | IsNullExpNode |
| 5 | SelectExpNode |
| 6 | BetweenExpNode |
| 7 | EscapeExpNode |
| 7 | NotExpNode |
| 7 | LikeExpNode |
| 8 | AndExpNode |
| 9 | OrExpNode |

