# Introduzione #

Nel corso dello sviluppo per la delivery odierna, vedi SinekartaDsRoadmap2, è stata implementata la firma PDF e si è presentato il problema della sua integrazione nel sistema attuale, questo ha portato ad un lavoro di refactoring nell'architettura del progetto.
Seguono alcuni esempi delle problematiche che hanno portato a rivedere l'implementazione:
  * le classi di dominio inizialmente sviluppate per la validazione della firma CMS potevano esser ora adattate alla gestione degli altri tipi di firma per limitare la ridondanze del codice
  * l'aggiornamento del model ha portato alla luce che diversi moduli del progetto potevano quindi esser riorganizzati semplificati per ottenere un unico protocollo generale di firma e verifica, pilotato dalle classi stesse di dominio
  * da una maggior comprensione delle firme CMS e PDF è emerso un modo più corretto per gestire l'applicazione e la verifica delle marche temporali
  * i processi di verifica finora implementati erano incompleti in quanto garantivano l'integrità delle firme, non l'attendibilità del firmatario
  * durante la ristrutturazione del model, non era completamente come sarebbe dovuta esser gestita la catena di certificazione nei service di firma e verifica per consentire (e non si può più rischiare di investire altro tempo in ulteriori ristrutturazioni)
Il pesante refactoring che ne è seguito ha portato alla luce la necessità di un'ultima analisi generale del lavoro svolto, al fine di stabilizzare le interfacce dei moduli di modo che non emerga più la necessità di metterci mano, nemmeno negli sviluppi futuri.


# Model #
  * DigestInfo: impronta ed algoritmo usato
  * SignatureType: tipi di firma supportati
  * SignatureDisposition: posizioni in cui è possibile inserire la firma
  * CertificateInfo: estensione di X509Certificate recante le informazioni di validazione, si veda di seguito
  * CertificateId: identifica univocamente un certificato in base ai dati disponibili: subjectUniqueID, signature oppure subjectPrincipal ed issuerPrincipal; usato come ID nelle mappature e nel riconoscimento dei certificati
  * CertificationSource: protocollo di reperimento di certificati verificati
  * SignatureInfo: firma digitale, algoritmo usato, certificato ed accesso alla relativa catena, dettagli della firma, altri attributi
    * CMSSignatureInfo
    * PDFSignatureInfo: estensione per la firma PDF
    * XMLSignatureInfo: estensione per la firma XML
    * TimeStampInfo: estensione per i timestamp, output per la verifica della marca
  * TsRequestInfo: input per le richieste ai servizi di marca temporale
  * VerifyInfo: output delle validazioni per ogni firma o marca


# Architettura del core #

  * SignatureService: unico protocollo di firma, verifica e marca, tutte le informazioni specifiche per il tipo di firma verranno indicate dal relativo DTO
    * CMSSignatureService
    * PDFSignatureService
    * XMLSignatureService

# Architettura di certificazione #
  * CertificateInfoComparator: supporta diversi ordinamenti per i certificati (es alfabetico, attendibilità per verifica, si veda di seguito
  * AbstractCertificationSource: implementazione di CertificationSource, generico fornitore dei certificati presenti in una qualunque fonte
    * TrustedKeyStore unità di immagazzinamento dei certificati, può esser usata per contenere quelli presenti in altri supporti (es. accedere ai (soli) certificati nelle smartcard anche quando queste non sono fisicamente disponibili)
      * FileSystemKeyStore
      * AlfrescoKeyStore
    * FakeCertificationSource base comune per le fonti non implementati ma il cui supporto è previsto dall'architettura
      * JavaKeytool: CA universamente accettate dalla JVM
      * SmartCardReader
      * USBToken
      * Webservice (?? vedere se necessario durante lo sviluppo della firma da WS)
  * CertificateChainBuilder: verifica dei certificati, generazione delle catene di certificazione
  * CertificationTree: struttura ad albero il mantenimento delle relazioni di firma tra i certificati
  * CertificationRepository: unità centrale di immagazzinamento dei certificati e controllo delle altre fonti, singleton
  * X509Provider: utility suite per la conversione e la verifica dei certificati
<b>Nota:</b> Alcune funzionalità di sicurezza, quale la gestione dei PENDING (si veda di seguito) certificate sono lasciate da implementare successivamente in quanto l'obbiettivo era quello di render operativa la firma il prima possibile.
L'intento è stato comunque quello di far subito le dovute osservazioni di sicurezza per evitare di incorrere in ulteriori modifiche ai protocolli.

## Inserimento dei certificati ##
Qualunque utente autenticato può inserire un certificato, dopodiché
  * se non self-signed e la catena viene ricostruita con successo viene inserito nel sistema immediatamente
  * se self-signed lo si considera relativo ad CA; servono permessi speciali (amministratore o RCS) per l'inserimento diretto, diversamente verranno marcate come PENDING, assieme a tutti i certificati intermedi che ne derivano, in attesa di approvazione
  * se la validazione fallisce (non sono noti tutti i certificati della catena) il certificato viene inserito in una partizione UNTRUSTED
  * in qualunque momento viene fatta una verifica lo stato di validità di un certificato può cambiare in base alle disponibilità corrente, e quelli da esso derivato di conseguenza

## Attendibilità delle fonti ##
Allo startup dell'applicazione vengono caricati i certificati immediatamente disponibili. Per loro stessa natura, le fonti da cui estrarre i certificati (specialmente le CA) hanno diversi livelli di attendibilità:
  1. JAVA\_KEYTOOL
  1. PHYSICAL\_DEVICE
  1. WEBSERVICE
  1. FILE\_SYSTEM\_KEYSTORE
  1. ALFRESCO\_KEYSTORE
L'accesso al file system sarà consentito solamente all'amministratore, gli altri utenti avranno la possibilità di generare i propri keystore su Alfresco.
Questo scenario può portare alla creazione di duplicati per cui il processo di validazione accederà prima ai certificati salvati nelle fonti più attendibili, ignorando le copie successive.


# Esiti della re-ingegnerizzazione #
  * il model ha assunto ora una struttura definitiva e flessibile
  * è possibile effettuare un refactoring del core e dei DTO per Alfresco con una ragionevole sicurezza che sia l'ultimo
  * l'aggiunta della firma XML può avvenire senza timore di incontrare altre difficoltà dal punto di vista dei protocolli, in quanto tutto viene ora pilotato dal model
  * in una fase successiva di sviluppo si può aggiungere a Sinekarta la gestione dei certificati solamente implementando i metodi astratti e costruendone i lati Alfresco
  * Sinekarta sarà ora abilitato a verificare non solo l'integrità delle firme digitali ma anche l'identità dei firmatari
  * le firme generate ora da Sinekarta conterranno l'intera catena di certificazione, questo implica che anche un tool di verifica esterno quale Dike, che non possiede tutti quelli intermedi sarà in grado di riconoscere la validità e l'identità della firma


# Roadmap per la delivery successiva #
|**OBIETTIVO**|**TEMPO STIMATO**|**SCADENZA STIMATA**|**NOTE**|**TEMPO EFFETTIVO**|**SCADENZA EFFETTIVA**|
|:------------|:----------------|:-------------------|:-------|:------------------|:---------------------|
|refactoring delle firme CMS e PDF|16|??/??|**|**|/ |
|protocollo unificato di timestamp|12|??/??|**|**|/ |
|firma XML|16|??/??|**|**|/ |
|adeguamento della GUI alle nuove funzionalità, es. pdf, disposizione firma/marca|8 |??/??|**|**|/ |
|accesso ai keystore su Alfresco da GUI|16|??/??|**|**|/ |
|funzionamento della smartcard da applet|16|??/??|**|**|/ |
|installer tramite amp|8-16|??/??|**|**|/ |