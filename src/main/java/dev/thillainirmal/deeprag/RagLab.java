package dev.thillainirmal.deeprag;
import org.springframework.stereotype.Service;import java.util.*;import java.util.stream.*;
@Service
public class RagLab{
 private final List<Doc> docs=List.of(
  new Doc("D1","Retries","engineering","Exponential backoff with jitter prevents synchronized retry storms. Cap attempts and honor deadlines."),
  new Doc("D2","Circuit breakers","engineering","Circuit breakers open after repeated failures, reject calls briefly, then probe recovery in half-open state."),
  new Doc("D3","Database incidents","operations","Connection pool exhaustion causes latency and timeouts. Inspect active connections before scaling."),
  new Doc("D4","Queue recovery","operations","Measure consumer lag and oldest message age. Isolate poison messages before adding consumers."),
  new Doc("D5","Error budgets","sre","Error budgets balance reliability and delivery. Freeze risky releases when burn rate exceeds policy."),
  new Doc("D6","Caching","engineering","Cache stampedes require request coalescing, randomized expiry and stale-while-revalidate."));
 public Result run(String query,String domain){
  List<String> expansions=List.of(query,query.replace("failure","outage")+" resilience", "How to mitigate "+query);
  String hypothetical="A strong answer discusses safeguards, signals, rollback, bounded retries and recovery for "+query;
  Map<String,Double> scores=new HashMap<>();List<Step> steps=new ArrayList<>();
  steps.add(new Step("1 Query rewrite",expansions.get(1)));
  for(int qi=0;qi<expansions.size();qi++){List<Scored> ranked=rank(expansions.get(qi),domain);for(int i=0;i<ranked.size();i++)scores.merge(ranked.get(i).doc.id,1.0/(60+i+1),Double::sum);}
  steps.add(new Step("2 Multi-query",expansions.size()+" perspectives fused"));
  rank(hypothetical,domain).forEach(s->scores.merge(s.doc.id,s.score*.03,Double::sum));steps.add(new Step("3 HyDE",hypothetical));
  steps.add(new Step("4 Metadata filter",domain==null||domain.equals("all")?"all domains":"domain="+domain));
  List<Scored> fused=scores.entrySet().stream().map(e->new Scored(find(e.getKey()),e.getValue()+lexical(query,find(e.getKey()).text)*.05)).sorted(Comparator.comparingDouble(Scored::score).reversed()).limit(4).toList();
  steps.add(new Step("5 Re-ranking","lexical relevance added after reciprocal-rank fusion"));
  List<Hit> hits=fused.stream().map(s->new Hit(s.doc.id,s.doc.title,s.doc.domain,compress(s.doc.text,query),Math.round(s.score*10000.0)/10000.0)).toList();
  steps.add(new Step("6 Compression","returned only evidence-bearing sentences"));
  return new Result(query,expansions,hits,steps,hits.isEmpty()?"No grounded evidence found.":"Grounded answer: "+hits.stream().limit(2).map(h->h.excerpt+" ["+h.id+"]").collect(Collectors.joining(" ")));
 }
 private List<Scored> rank(String q,String domain){return docs.stream().filter(d->domain==null||domain.equals("all")||d.domain.equals(domain)).map(d->new Scored(d,lexical(q,d.text+" "+d.title))).filter(s->s.score>0).sorted(Comparator.comparingDouble(Scored::score).reversed()).toList();}
 private double lexical(String a,String b){Set<String>x=tokens(a),y=tokens(b);long n=x.stream().filter(y::contains).count();return x.isEmpty()?0:(double)n/x.size();}
 private Set<String> tokens(String s){return Arrays.stream(s.toLowerCase().replaceAll("[^a-z0-9 ]"," ").split("\\s+")).filter(t->t.length()>3).collect(Collectors.toSet());}
 private String compress(String text,String q){return Arrays.stream(text.split("(?<=\\.) ")).max(Comparator.comparingDouble(s->lexical(q,s))).orElse(text);}
 private Doc find(String id){return docs.stream().filter(d->d.id.equals(id)).findFirst().orElseThrow();}
 record Doc(String id,String title,String domain,String text){} record Scored(Doc doc,double score){}
 public record Hit(String id,String title,String domain,String excerpt,double score){} public record Step(String pattern,String output){} public record Result(String query,List<String> expandedQueries,List<Hit> hits,List<Step> trace,String answer){}
}
