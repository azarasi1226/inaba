# Projector
Projectorでは発行されたEventを元にDBに射影(更新的な意味)します。  

結果整合性でDBに書き込まれるためクライアントからはポーリング問い合わせが必須です。

## 補足：Lookup系
Lookup系のProjectorはSubscriptionEventProcessorを利用し、イベントが発行された瞬間に射影するようにしています。  
CQRS/ESの結果整合性に違反するような動きとなるためパフォーマンスは落ちますが、強い整合性をかけられます。

主に集約間の整合性調整のために使用されます。